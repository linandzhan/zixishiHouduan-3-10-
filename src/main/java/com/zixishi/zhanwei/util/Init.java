package com.zixishi.zhanwei.util;

import com.zixishi.zhanwei.config.authorization.annotation.Authorization;
import com.zixishi.zhanwei.config.authorization.annotation.RolePermission;
import com.zixishi.zhanwei.mapper.ClockMapper;
import com.zixishi.zhanwei.mapper.PermissionMapper;
import com.zixishi.zhanwei.mapper.RoleMapper;
import com.zixishi.zhanwei.mapper.SeatMapper;
import com.zixishi.zhanwei.model.*;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import static cn.hutool.core.util.ClassUtil.getClassLoader;



@Component
@EnableScheduling
public class Init {
    @Resource
    private PermissionMapper permissionMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private ClockMapper clockMapper;
    @Resource
    private SeatMapper seatMapper;


    public  static Set<Class<?>> getClasses(String packageName) throws IOException {
        Set<Class<?>> classSet = new HashSet<>();
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            if (url != null) {
                String protocol = url.getProtocol();
                if (protocol.equals("file")) {
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if (protocol.equals("jar")) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        }

        return classSet;
    }


    private static   void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory();
            }
        });
        for (File file : files) {
            String fileName = file.getName();
            if (file.isFile()) {
                String className = fileName.substring(0, fileName.lastIndexOf("."));
                if (StringUtils.isNotEmpty(packageName)) {
                    className = packageName + "." + className;
                }
                doAddClass(classSet, className);
            } else {
                String subPackagePath = fileName;
                if (StringUtils.isNotEmpty(packagePath)) {
                    subPackagePath = packagePath + "/" + subPackagePath;
                }
                String subPackageName = fileName;
                if (StringUtils.isNotEmpty(packageName)) {
                    subPackageName = packageName + "." + subPackageName;
                }
                addClass(classSet, subPackagePath, subPackageName);
            }
        }
    }




    private static    void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    public    Class<?> loadClass(String className) {
        return loadClass(className, true);
    }


    public static   Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 自动配置权限
     */
    @PostConstruct
    public  void peizhi() throws IOException {
        System.out.println("启动配置111111");
        Set<Class<?>> controllers = getClasses("com.zixishi.zhanwei.controller");


        List<Permission> permissions = permissionMapper.searchPath();
        List<String> paths = new ArrayList<>();
        for (Permission permission : permissions) {
            paths.add(permission.getPath());
        }
        for (Class<?> controller : controllers) {
            Method[] methods = controller.getMethods();
//            System.out.println(controller.getSimpleName());
            String name = controller.getSimpleName();
            String[] split = name.split("Controller");
            String className = split[0].toLowerCase();
            for (Method method : methods) {
                String path = "/" + className + "/" + method.getName();
                Permission permission = permissionMapper.attach(path);
                if(method.getAnnotation(Authorization.class) != null) {
                    //需要进行登录校验的，进行获取
                    if(!paths.contains(path)) {
                        permissionMapper.save(path);
                    }
                }

                //根据角色配置对应的权限
                RolePermission rolePermission = method.getAnnotation(RolePermission.class);
                Authorization annotation = method.getAnnotation(Authorization.class);
                System.out.println(annotation);
                if(rolePermission != null) {
                    List<Role> roles = roleMapper.search();
                    for (Role role : roles) {
                        String[] values = rolePermission.value();


                        for (int i = 0; i < values.length; i++) {
                            if (role.getRolename().equals(values[i])) {
                                List<Permission> oldPermissions = permissionMapper.searchByRole(role);
                                List<String> oldPaths = new ArrayList<>();
                                for (Permission oldPermission : oldPermissions) {
                                    oldPaths.add(oldPermission.getPath());
                                }
                                if(!oldPaths.contains(path)) {
                                    permissionMapper.bind(role,permission);
                                }
                            }
                        }

                    }

                }
            }
        }




    }


    /**
     * 检查忘记打签退卡的人，是否已经到了预约结束的时间.如果是，需要帮它打上退卡时间。
     */
    @Scheduled(fixedRate = 10000)
    private void checkReservation() {
        List<Clock> clocks = clockMapper.searchEndTimeIsNull();
        for (Clock clock : clocks) {
            Reservation reservation = clock.getReservation();
            if(reservation.getEndTime().isBefore(LocalTime.now())) {
                Clock newClock = new Clock();
                newClock.setId(clock.getId());
                newClock.setEndTime(LocalDateTime.now());
                newClock.setLength(Duration.between(clock.getSigninTime(), LocalDateTime.now()).toMinutes());
                clockMapper.update(newClock);
                Seat seat = new Seat();
                seat.setId(clock.getSeat().getId());
                seat.setStatus(false);
                seatMapper.updateStaus(seat);
            }
        }

    }

}

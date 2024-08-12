package com.epam.rd.autotasks.springstatefulcalc.init;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

import javax.servlet.ServletException;
import java.io.File;

public class Main {
    public static void main(String[] args) throws ServletException, LifecycleException {
        int port = 8080;
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);

        String webappDirLocation = "src/main/webapp/";
        File webappDir = new File(webappDirLocation);
        if (!webappDir.exists() || !webappDir.isDirectory()) {
            throw new IllegalStateException("Webapp directory does not exist: " + webappDir.getAbsolutePath());
        }

        StandardContext ctx = (StandardContext) tomcat.addWebapp("", webappDir.getAbsolutePath());
        System.out.println("Configuring app with basedir: " + webappDir.getAbsolutePath());

        File additionWebInfClasses = new File("target/classes");
        if (additionWebInfClasses.exists() && additionWebInfClasses.isDirectory()) {
            WebResourceRoot resources = new StandardRoot(ctx);
            resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
            ctx.setResources(resources);
        } else {
            throw new IllegalStateException("Target classes directory does not exist: " + additionWebInfClasses.getAbsolutePath());
        }

        tomcat.start();
        tomcat.getServer().await();
    }
}

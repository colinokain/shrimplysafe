/*
 * The MIT License
 *
 * Copyright 2022 colin.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.uah.hackathon2022;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener
        implements ServletContextAttributeListener, ServletContextListener {

    private ServletContext context = null;

    public void attributeAdded(ServletContextAttributeEvent scab) {
        System.out.println("An attribute was added to the "
                + "ServletContext object");
    }

    public void attributeRemoved(ServletContextAttributeEvent scab) {
        System.out.println("An attribute was removed from "
                + "the ServletContext object");
    }

    public void attributeReplaced(ServletContextAttributeEvent scab) {
        System.out.println("An attribute was replaced in the "
                + "ServletContext object");
    }

    public void contextDestroyed(ServletContextEvent event) {

        try {
            com.mysql.cj.jdbc.AbandonedConnectionCleanupThread.uncheckedShutdown();
        } catch (Throwable t) {
        }
        // This manually deregisters JDBC driver, which prevents Tomcat 7 from complaining about memory leaks
        Enumeration<java.sql.Driver> drivers = java.sql.DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            java.sql.Driver driver = drivers.nextElement();
            try {
                java.sql.DriverManager.deregisterDriver(driver);
            } catch (Throwable t) {
            }
        }
        try {
            Thread.sleep(2000L);
        } catch (Exception e) {
        }

    }

    public void contextInitialized(ServletContextEvent event) {
        this.context = event.getServletContext();

        Iterator<Driver> driversIterator = ServiceLoader.load(Driver.class).iterator();
        while (driversIterator.hasNext()) {
            try {
                // Instantiates the driver
                driversIterator.next();
            } catch (Throwable t) {
                event.getServletContext().log("JDBC Driver registration failure.", t);
            }

            System.out.println("ShrimplySafe initialized.");

        }

    }
}

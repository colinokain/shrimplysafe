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

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextListener
    implements ServletContextAttributeListener,ServletContextListener  {

  private ServletContext context = null;

  //This method is invoked when an attribute
  //is added to the ServletContext object
  public void attributeAdded (ServletContextAttributeEvent scab)
  {
    System.out.println("An attribute was added to the " +
      "ServletContext object");
  }

  //This method is invoked when an attribute
  //is removed from the ServletContext object
  public void attributeRemoved (ServletContextAttributeEvent scab)
  {
    System.out.println("An attribute was removed from " +
      "the ServletContext object");
  }

  //This method is invoked when an attribute
  //is replaced in the ServletContext object
  public void attributeReplaced (ServletContextAttributeEvent scab)
  {
    System.out.println("An attribute was replaced in the " +
      "ServletContext object");
  }


public void contextDestroyed(ServletContextEvent event)
  {

    //Output a simple message to the server's console
    System.out.println("The Simple Web App. Has Been Removed");
    this.context = null;

  }


  //This method is invoked when the Web Application
  //is ready to service requests

  public void contextInitialized(ServletContextEvent event)
  {
    this.context = event.getServletContext();

    //Output a simple message to the server's console
    System.out.println("The Simple Web App. Is Ready");

  }

}

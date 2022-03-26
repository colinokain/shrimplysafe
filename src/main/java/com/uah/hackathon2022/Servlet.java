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


import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author Colin O'Kain
 */
@WebServlet
public class Servlet extends HttpServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String path = request.getPathInfo();
        if(path == null)
            return;
        
        response.setCharacterEncoding("UTF-8");
        JSONObject responseJSON = new JSONObject();
        
        try
        {
            switch(path)
            {
                case "/test":
                    System.out.println("Here");
                    responseJSON.put("success", "balls");
                    break;
                default:
                    responseJSON.put("error", "Could not find location");
            }
        }
        catch(Exception e)
        {
            
        }
        sendData(response, responseJSON.toString(), "", 1, 1);
        
    }
    
    private void sendData(HttpServletResponse response, String jsonResponse, String type, long startTime, int id)
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        try (PrintWriter writer = response.getWriter())
        {
            writer.write((jsonResponse));
            writer.flush();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
    }
    
}

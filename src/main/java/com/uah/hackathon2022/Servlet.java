/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

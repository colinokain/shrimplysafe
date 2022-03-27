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


import com.uah.hackathon2022.Database.DatabaseConnection;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import java.security.MessageDigest;
import java.sql.SQLException;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONArray;
import org.json.JSONException;

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
                case "/addUser":
                    responseJSON = addUser(request);
                    break;
                case "/getUser":
                    responseJSON = getUser(request);
                    break;
                case "/updatePassword":
                    responseJSON = updatePassword(request);
                    break;
                case "/getValidPassword":
                    String username = request.getParameter("username");
                    responseJSON.put("hashValue", hashSha256(username.substring(0, username.length()/2) + hashSha256(request.getParameter("password")) + hashSha256(username.substring(username.length()/2 + 1, username.length()))));
                    responseJSON.put("success", "true");
                    responseJSON.put("username", username);
                    responseJSON.put("valid_user", "true");
                    responseJSON.put("password_auth", "true");
                    responseJSON.put("callback_data", (request.getParameter("callback_data") != null ? request.getParameter("callback_data") : new JSONArray()));                    
                    break;
                default:
                    responseJSON.put("error", "Could not find location");
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
        }
        sendData(response, responseJSON.toString(), "", 1, 1);
        
    }
    
    private JSONObject addUser(HttpServletRequest request) throws JSONException, SQLException
    {
        JSONObject responseJSON = new JSONObject();
        String username = request.getParameter("username");
        DatabaseConnection.getInstance().addUser(username);
        DatabaseConnection.getInstance().addPassword(hashSha256(username.substring(0, username.length()/2)).get("result").toString() + hashSha256(request.getParameter("password")).get("result").toString() + hashSha256(username.substring(username.length()/2 + 1, username.length())).get("result").toString());
        responseJSON.put("hashValue", hashSha256(username.substring(0, username.length()/2) + hashSha256(request.getParameter("password")) + hashSha256(username.substring(username.length()/2 + 1, username.length()))));
        responseJSON.put("success", "true");
        responseJSON.put("username", username);
        responseJSON.put("valid_user", "true");
        responseJSON.put("password_auth", "true");
        responseJSON.put("callback_data", (request.getParameter("callback_data") != null ? request.getParameter("callback_data") : new JSONArray()));
        
        return responseJSON;
    }
   
    private JSONObject getUser(HttpServletRequest request) throws JSONException, SQLException
    {
        JSONObject responseJSON = new JSONObject();
        String username = request.getParameter("username");
        String password = hashSha256(username.substring(0, username.length()/2)).get("result").toString() + hashSha256(request.getParameter("password")).get("result").toString() + hashSha256(username.substring(username.length()/2 + 1, username.length())).get("result").toString();
        
        JSONObject queryResults = DatabaseConnection.getInstance().getUser(username, password);
        
        return queryResults;
    }
    
    private JSONObject updatePassword(HttpServletRequest request) throws JSONException, SQLException
    {
        JSONObject responseJSON = new JSONObject();
        String username = request.getParameter("username");
        String oldPassword = hashSha256(username.substring(0, username.length()/2)).get("result").toString() + hashSha256(request.getParameter("password")).get("result").toString() + hashSha256(username.substring(username.length()/2 + 1, username.length())).get("result").toString();
        String newPassword = hashSha256(username.substring(0, username.length()/2)).get("result").toString() + hashSha256(request.getParameter("newPassword")).get("result").toString() + hashSha256(username.substring(username.length()/2 + 1, username.length())).get("result").toString();
        
        JSONObject queryResults = DatabaseConnection.getInstance().updatePassword(username, oldPassword, newPassword);
        
        return queryResults;
    }
    
    private JSONObject hashSha256(String code)
    {
        JSONObject returnVal = new JSONObject();
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(code.getBytes("UTF-8"));
            returnVal.put("result", DatatypeConverter.printHexBinary(hash));
        }catch(Exception ex) {
            ex.printStackTrace();
        }
        return returnVal;
    }
    
    private void sendData(HttpServletResponse response, String jsonResponse, String type, long startTime, int id)
    {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        
        try (PrintWriter writer = response.getWriter())
        {
            writer.write(jsonResponse);
            writer.flush();
        }
        catch(Exception ex)
        {
            System.err.println(ex);
        }
    }
    
    private static String generatePassword(int length, boolean useNumbers, boolean useSpecialCharacters) {
        
        String[] capitalCaseLetters = {"Q","W","E","R","T","Y","U","I","O","P","A","S","D","F","G","H","J","K","L","Z","X","C","V","B","N","M"};
        String[] lowerCaseLetters = {"q","w","e","r","t","y","u","i","o","p","a","s","d","f","g","h","j","k","l","z","x","c","v","b","n","m"};
        String[] specialCharacters = {"$","#","!","@","%","^","&","*"};
        String[] numbers = {"1","2","3","4","5","6","7","8","9","0"};
        
        String password = "";
        
        for(int i = 0; i < length; i++) {
            int num =  (int)(Math.random()*(4)+1);
            
            switch(num)
            {
                case 1:
                    password = password + capitalCaseLetters[(int)(Math.random()*(26) + 0.5)];  
                    break;
                case 2:
                    password = password + lowerCaseLetters[(int)(Math.random()*(26) + 0.5)];  
                    break;
                case 3:
                    if(useSpecialCharacters) 
                        password = password + specialCharacters[(int)(Math.random()*(7) + 0.5)];
                    break;
                case 4:
                    if(useNumbers)
                        password = password + numbers[(int)(Math.random()*(9) + 0.5)];
                    break;
                default:
                    i -= 1;
            }
        }
        
        return password;
    }
    
}

package com.revature.auth;
import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revature.common.Error;
import com.revature.common.exceptions.AuthenticationException;
import com.revature.common.exceptions.DataSourceException;
import com.revature.common.exceptions.InvalidRequestException;
import com.revature.users.UserResponse;



public class AuthServlet extends HttpServlet {

    
    
    private final AuthService authService;
    private final ObjectMapper objectMapper;
    
    

    public AuthServlet (AuthService authService, ObjectMapper objectMapper){
        this.authService=authService;
        this.objectMapper=objectMapper;
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        

        try{

            Credentials credentials = objectMapper.readValue(req.getInputStream(), Credentials.class);

            UserResponse loggedInUserResponse = authService.authenticate(credentials);
            resp.setStatus(200);
            resp.getWriter().write(objectMapper.writeValueAsString(loggedInUserResponse));

        }catch(JsonMappingException e){

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch(InvalidRequestException e){

            resp.setStatus(400);

            Error error = new Error(400, e.getMessage());
            

            resp.getWriter().write(objectMapper.writeValueAsString(error));
            
        }catch(AuthenticationException e){
            
            resp.setStatus(401); 

            Error error = new Error(401, e.getMessage());
            

            resp.getWriter().write(objectMapper.writeValueAsString(error));

        }catch(DataSourceException e){

            resp.setStatus(500); 

            Error error = new Error(500, e.getMessage());

            resp.getWriter().write(objectMapper.writeValueAsString(error));
        }


    }
}

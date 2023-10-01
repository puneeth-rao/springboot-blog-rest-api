package com.springboot.blog.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class AppUtils {
    @Value("${app.jwt.exp-time-days}")
    Integer jwtExpirationDays;

    public Boolean isAdmin(Authentication authentication){
        for(GrantedAuthority a : authentication.getAuthorities()){
            if(a.getAuthority().equalsIgnoreCase(AppConstants.ROLE_ADMIN))
                return true;
        }
        return false;
    }

    //jwt expires at 2.a.m after 2 days when it's created
    public Date getJwtExpirationDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 2);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DAY_OF_MONTH, jwtExpirationDays);

        return c.getTime();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycompany.mavenhelloservice.impl;

import com.mycompany.mavenhelloservice.api.HelloService;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

/**
 *
 * @author xumak-pc
 */
@Component(name="howdy-service")
@Service
public class HowdyImpl implements HelloService{

    @Override
    public String sayHello(String name) {
        return "Howdy" + name;
    }
    
}

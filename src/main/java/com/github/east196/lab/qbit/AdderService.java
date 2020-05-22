package com.github.east196.lab.qbit;

import io.advantageous.qbit.annotation.PathVariable;
import io.advantageous.qbit.annotation.RequestMapping;

@RequestMapping("/adder-service")
public class AdderService {


    @RequestMapping("/add/{0}/{1}")
    public int add(@PathVariable int a, @PathVariable int b) {

        return a + b;
    }
}
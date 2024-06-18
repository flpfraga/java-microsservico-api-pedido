package br.com.felipefraga.pedidos.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    public ModelMapper configModelMapper(){
        return new ModelMapper();
    }
}

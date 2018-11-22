package br.com.clearinvest.clivserver.cucumber.stepdefs;

import br.com.clearinvest.clivserver.ClivServerApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ClivServerApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}

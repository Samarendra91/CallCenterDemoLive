/**
 * 
 */
package com.tavant.callccenter.controller;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import com.tavant.callccenter.sevice.CallConfig;

/**
 * @author samarendra.sahoo
 * 
 *         ApplicationReadyEventListener: Triggers creation of all call Configurations. This bean is
 *         called post all beans definitions are loaded and application s ready to serve requests.
 *
 */
@Component
public class ApplicationReadyEventListener implements ApplicationListener<ApplicationReadyEvent> {

  @Override
  public void onApplicationEvent(ApplicationReadyEvent event) {

    // configure call config and instantiate call queue and employees
    loadCallConfig();

  }

  /**
   * 
   */
  private void loadCallConfig() {
    CallConfig.configure();
  }

}

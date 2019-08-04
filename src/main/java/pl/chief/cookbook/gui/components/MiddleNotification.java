package pl.chief.cookbook.gui.components;

import com.vaadin.flow.component.notification.Notification;

public class MiddleNotification extends Notification {
   public MiddleNotification(){
       this.setDuration(5000);
       this.setPosition(Position.MIDDLE);
   }
}

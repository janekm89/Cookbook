package pl.chief.cookbook.gui.components;

import com.vaadin.flow.component.notification.Notification;

public class MiddleNotification extends Notification {
   public MiddleNotification(){
       this.setDuration(3000);
       this.setPosition(Position.MIDDLE);
   }

   public MiddleNotification(String message){
       this();
       this.setText(message);
   }
    public MiddleNotification(String message, int duration){
        this(message);
        this.setDuration(duration);
    }
}

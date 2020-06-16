package zju.plugin;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationDisplayType;
import com.intellij.notification.NotificationGroup;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;

public class ProjectNotifier {
    private final NotificationGroup NOTIFICATION_GROUP = new NotificationGroup("Information", NotificationDisplayType.BALLOON, true);

    public Notification notify(String content) {
        return notify(null, content);
    }

    public Notification notify(Project project, String content) {
        final Notification notification = NOTIFICATION_GROUP.createNotification(content, NotificationType.INFORMATION);
        notification.notify(project);
        return notification;
    }
}


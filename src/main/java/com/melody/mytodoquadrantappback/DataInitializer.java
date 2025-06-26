package com.melody.mytodoquadrantappback;

import com.melody.mytodoquadrantappback.model.Task;
import com.melody.mytodoquadrantappback.service.TaskService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

//    private final TaskService taskService;
//
//    public DataInitializer(TaskService taskService) {
//        this.taskService = taskService;
//    }

    @Override
    public void run(String... args) throws Exception {
        // 清空資料
//        taskService.deleteAllTasks();

        // 建立預設 Task
//        Task defaultTask = new Task();
//
//        defaultTask.setTitle("預設任務");
//        defaultTask.setImportant(null);
//        defaultTask.setUrgent(null);
//        defaultTask.setCreatedAt(Instant.now());
//        defaultTask.setDescription("這是系統啟動時自動建立的預設任務");
//        defaultTask.setDueDate(Instant.now().plus(java.time.Duration.ofDays(7))); // 預設到期日為 7 天後
//        defaultTask.setCompleted(false);
//        defaultTask.setUserId("default-user-id"); // 預設使用者 ID，實際應根據需求設定
//        defaultTask.setOrderIndex(0); // 預設排序索引
//
//        taskService.createTask(defaultTask);

//        System.out.println("資料庫已清空並新增預設任務");
    }
}

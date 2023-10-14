package com.estherpeixoto.todolist.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity(name = "tb_tasks")
public class TaskModel {

    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Column(length = 50)
    private String title;

    @Column(length = 255)
    private String description;

    @Column(length = 6)
    private String priority;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private UUID idUser;

    @CreationTimestamp
    private LocalDateTime created_at;

    public void setTitle(String title) throws Exception {
        if (title.length() > 50) {
            throw new Exception("O campo title deve conter no máximo 50 caracteres.");
        }

        this.title = title;
    }

    public void setDescription(String description) throws Exception {
        if (description.length() > 255) {
            throw new Exception("O campo description deve conter no máximo 255 caracteres.");
        }

        this.description = description;
    }

}

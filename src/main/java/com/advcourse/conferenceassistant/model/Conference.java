package com.advcourse.conferenceassistant.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Conference {
    @Id
    @GeneratedValue
    private Long id;

    private String theme;
    private String description;
    private LocalDateTime start;
    private LocalDateTime end;
    private String address;


}

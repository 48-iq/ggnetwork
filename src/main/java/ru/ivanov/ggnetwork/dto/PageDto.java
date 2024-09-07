package ru.ivanov.ggnetwork.dto;

import lombok.*;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageDto<T> {
    private Integer page;
    private Integer size;
    private Integer total;
    private List<T> data;
}

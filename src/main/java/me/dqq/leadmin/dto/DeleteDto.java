package me.dqq.leadmin.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeleteDto {
    private List<Long> ids;
}

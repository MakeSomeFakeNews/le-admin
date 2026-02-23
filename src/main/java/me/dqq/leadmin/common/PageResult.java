package me.dqq.leadmin.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private List<T> records;
    private long total;

    public static <T> PageResult<T> of(List<T> records, long total) {
        return new PageResult<>(records, total);
    }
}

package com.creditstate.demo.entity.common;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author weiming.zhu
 * @date 2020/4/28 14:53
 */
@Data
@Builder(toBuilder = true)
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    private int index = 0;
    private int size = 10;
}

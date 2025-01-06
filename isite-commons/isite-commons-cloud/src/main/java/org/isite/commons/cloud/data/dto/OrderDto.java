package org.isite.commons.cloud.data.dto;

import lombok.Getter;
import lombok.Setter;
import org.isite.jpa.data.Direction;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderDto {
    /**
     * 排序字段
     */
    @NotBlank
    private String field;
    /**
     * 排序方向
     */
    @NotNull
    private Direction direction;

}
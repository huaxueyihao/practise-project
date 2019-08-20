package source.unsafe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@AllArgsConstructor
@ToString
@Slf4j
public class Rectangle {

    private int height;

    private int width;

    private Rectangle subRectangle;

    static int count = 0;

    public Rectangle() {
        log.info("Rectangle构造函数");
    }
}

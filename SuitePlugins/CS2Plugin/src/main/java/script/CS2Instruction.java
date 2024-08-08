package script;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor @Getter @Setter
public class CS2Instruction {

    private final int opcode;
    private String name;

}

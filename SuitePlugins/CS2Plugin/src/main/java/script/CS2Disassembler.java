package script;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class CS2Disassembler {

    private final CS2Instructions instructions = new CS2Instructions();

    public CS2Disassembler() {
        instructions.init();
    }

    private boolean isJump(int opcode)
    {
        switch (opcode)
        {
            case CS2Opcode.JUMP:
            case CS2Opcode.IF_ICMPEQ:
            case CS2Opcode.IF_ICMPGE:
            case CS2Opcode.IF_ICMPGT:
            case CS2Opcode.IF_ICMPLE:
            case CS2Opcode.IF_ICMPLT:
            case CS2Opcode.IF_ICMPNE:
                return true;
            default:
                return false;
        }
    }

    private boolean[] needLabel(CS2Script script)
    {
        int[] instructions = script.getInstructions();
        int[] iops = script.getIntOrphands();
        Map<Integer, Integer>[] switches = script.getSwitches();

        boolean[] jumped = new boolean[instructions.length];

        for (int i = 0; i < instructions.length; i++)
        {
            int opcode = instructions[i];
            int iop = iops[i];

            if (opcode == CS2Opcode.SWITCH)
            {
                Map<Integer, Integer> switchMap = switches[iop];

                for (Map.Entry<Integer, Integer> entry : switchMap.entrySet())
                {
                    int offset = entry.getValue();

                    int to = i + offset + 1;
                    assert to >= 0 && to < instructions.length;
                    jumped[to] = true;
                }
            }

            if (!isJump(opcode))
            {
                continue;
            }

            int to = i + iop + 1;
            assert to >= 0 && to < instructions.length;
            jumped[to] = true;

        }
        return jumped;
    }

    public String disassemble(CS2Script script)
    {
        int[] instructions = script.getInstructions();
        int[] iops = script.getIntOrphands();
        String[] sops = script.getStringOrphands();
        Map<Integer, Integer>[] switches = script.getSwitches();

        assert iops.length == instructions.length;
        assert sops.length == instructions.length;

        boolean[] jumps = needLabel(script);

        StringBuilder writer = new StringBuilder();
        writerHeader(writer, script);

        for (int i = 0; i < instructions.length; ++i)
        {
            int opcode = instructions[i];
            int iop = iops[i];
            String sop = sops[i];

            CS2Instruction ins = this.instructions.find(opcode);
            if (ins == null)
            {
                log.warn("Unknown instruction {} in script {}", opcode, script.id);
            }

            if (jumps[i])
            {
                // something jumps here
                writer.append("LABEL").append(i).append(":\n");
            }

            String name;
            if (ins != null && ins.getName() != null)
            {
                name = ins.getName();
            }
            else
            {
                name = String.format("%03d", opcode);
            }

            writer.append(String.format("   %-22s", name));

            if (shouldWriteIntOperand(opcode, iop))
            {
                if (isJump(opcode))
                {
                    writer.append(" LABEL").append(i + iop + 1);
                }
                else
                {
                    writer.append(" ").append(iop);
                }
            }

            if (sop != null)
            {
                writer.append(" \"").append(sop).append("\"");
            }

            if (opcode == CS2Opcode.SWITCH)
            {
                Map<Integer, Integer> switchMap = switches[iop];

                for (Map.Entry<Integer, Integer> entry : switchMap.entrySet())
                {
                    int value = entry.getKey();
                    int jump = entry.getValue();

                    writer.append("\n");
                    writer.append("      ").append(value).append(": LABEL").append(i + jump + 1);
                }
            }

            writer.append("\n");
        }

        return writer.toString();
    }

    private boolean shouldWriteIntOperand(int opcode, int operand)
    {
        if (opcode == CS2Opcode.SWITCH)
        {
            // table follows instruction
            return false;
        }

        if (operand != 0)
        {
            // always write non-zero operand
            return true;
        }

        switch (opcode)
        {
            case CS2Opcode.ICONST:
            case CS2Opcode.ILOAD:
            case CS2Opcode.SLOAD:
            case CS2Opcode.ISTORE:
            case CS2Opcode.SSTORE:
                return true;
        }

        // int operand is not used, don't write it
        return false;
    }

    private void writerHeader(StringBuilder writer, CS2Script script)
    {
        int id = script.id;
        int intStackCount = script.getIntStackCount();
        int stringStackCount = script.getStringStackCount();
        int localIntCount = script.getLocalIntCount();
        int localStringCount = script.getLocalStringCount();

        writer.append(".id                 ").append(id).append('\n');
        writer.append(".int_stack_count    ").append(intStackCount).append('\n');
        writer.append(".string_stack_count ").append(stringStackCount).append('\n');
        writer.append(".int_var_count      ").append(localIntCount).append('\n');
        writer.append(".string_var_count   ").append(localStringCount).append('\n');
    }

}

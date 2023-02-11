package simulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SimFunc {
    public static final int HLT = 0;
    public static final int LDR = 1; //Load Register From Memory
    public static final int STR = 2; //Store Register To Memory
    public static final int LDA = 3; //Load Register with Address
    public static final int LDX = 41; //Load Index Register from Memory
    public static final int STX = 42; //Store Index Register to Memory
    public static final int pcZero = 10;
    public static final int pcNotZero = 11;
    public static int indexRegister1;
    public static int indexRegister2;
    public static int indexRegister3;
    public static int[] memory = new int[2048];
    public static int[] gpr = new int[4];
    public static int pc;
    public static int mar;
    public static int ir;
    public static int mbr;
    public static int mfr;
    public static boolean run = false;

    public static int counter;


    // execute: cases for each opcode which will run the respective instruction.
    public static void execute(Instructions instructions) {
        if (instructions.op_de == HLT) {//0
            HLT();
        }
        if (instructions.op_de == LDR) {//1
            LDR(instructions);
        }
        if (instructions.op_de == STR) {//2
            STR(instructions);
        }
        if (instructions.op_de == LDA) {//3
            LDA(instructions);
        }
        if (instructions.op_de == LDX) {//41
            LDX(instructions);
        }
        if (instructions.op_de == STX) {//42
            STX(instructions);
        }
        if (instructions.op_de == pcZero) {//10
            pcZero(instructions);
        }
        if (instructions.op_de == pcNotZero) {//11
            pcNotZero(instructions);
        }
    }


    // IPL load the instruction file, and parse through each line to get the address and instruction.
    // then add them into the memory.
    public static void IPL() {
        initialize();
        File file = new File(".\src\IPL.txt");
        String line;
        int address;
        int instruction;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            line = br.readLine();
            while (line != null) {
                address = Integer.parseInt(line.substring(0, 4), 16);
                instruction = Integer.parseInt(line.substring((line.length() - 3)), 16);
                memory[address] = instruction;
                line = br.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //initialize the database
    private static void initialize() {
        indexRegister1 = 0;
        indexRegister2 = 0;
        indexRegister3 = 0;

        int initNum = Integer.parseInt("0000000000000");
        for (int i = 0; i < 2048; i++) {
            memory[i] = initNum;
        }
        gpr = new int[4];

        pc = 0;
        mar = 0;
        ir = 0;
        mbr = 0;
        mfr = 0;
    }

    //run button function
    public static void run() {
        Instructions instructions;
        while (true) {
            fetchpc2mbr();
            instructions = decode(ir);
            execute(instructions);
            if (!run) break;
            if (pc == 2048) {
                mfr = 8;
                run = false;
                break;
            }
        }
    }

    //single instruction:load ir into mbr, only decodes, then executes without fetching.
    public static Instructions singleStep() { //
        Instructions instructions;
        ir = mbr;
        instructions = decode(ir);
        execute(instructions);
        return instructions;
    }

    //decoding ir
    public static Instructions decode(int ir) {
        return new Instructions(ir);
    }

    //fetch pc -> mbr:
    // 1.address in pc copied to the mar
    // 2.pc point to the next instruction(pc++)
    // 3.instruction found based on MAR's address, which is then copied by mbr.
    // 4.mbr instruction is then copied by ir.
    public static void fetchpc2mbr() {
        mar = pc;
        mbr = memory[mar];
        ir = mbr;
        pc++;
    }

    public static int FindEA(Instructions instructions) { //Find EA
        int addr, indirect, indexNum, address = 0;
        addr = instructions.addr_de;
        indirect = instructions.i_de;
        indexNum = instructions.ixr_de;
        if (indirect == 0) {
            if (indexNum == 0) {
                address = addr;
            } else {
                switch (indexNum) {
                    case 1:
                        address = indexRegister1 + addr;
                        break;
                    case 2:
                        address = indexRegister2 + addr;
                        break;
                    case 3:
                        address = indexRegister3 + addr;
                        break;
                }
            }
        }
        if (indirect == 1) {
            if (indexNum == 0) {
                address = memory[addr];
            } else {
                switch (indexNum) {
                    case 1:
                        address = memory[indexRegister1 + addr];
                        break;
                    case 2:
                        address = memory[indexRegister2 + addr];
                        break;
                    case 3:
                        address = memory[indexRegister3 + addr];
                        break;
                }
            }
        }
        return address;
    }


    public static void HLT() {
        // Do nothing
    }

    //Load Register From Memory
    public static void LDR(Instructions instructions) {
        int address;
        int memVal;
        address = FindEA(instructions);
        mar = address;
        memVal = memory[address];
        gpr[instructions.gpr_de] = memVal;
        mbr = memVal;
    }

    //Store Register To Memory
    public static void STR(Instructions instructions) {
        int address;
        address = FindEA(instructions);
        memory[address] = gpr[instructions.gpr_de];
        mbr = memory[address];
    }

    //Load Register with Address
    public static void LDA(Instructions instructions) {
        int address = instructions.addr_de;
        if (instructions.gpr_de == 0) {
            gpr[0] = address;
        }
        if (instructions.gpr_de == 1) {
            gpr[1] = address;
        }
        if (instructions.gpr_de == 2) {
            gpr[2] = address;
        }
        if (instructions.gpr_de == 3) {
            gpr[3] = address;
        }
    }

    public static void LDX(Instructions instructions) {
        int address;
        address = FindEA(instructions);
        mar = address;
        switch (instructions.ixr_de) {
            case 1:
                indexRegister1 = memory[address];
                break;
            case 2:
                indexRegister2 = memory[address];
                break;
            case 3:
                indexRegister3 = memory[address];
                break;
        }
        mbr = memory[address];

    }

    //Load Index Register from Memory
    public static void STX(Instructions instructions) {
        int address;
        address = FindEA(instructions);
        switch (instructions.ixr_de) {
            case 1:
                memory[address] = indexRegister1;
                break;
            case 2:
                memory[address] = indexRegister2;
                break;
            case 3:
                memory[address] = indexRegister3;
                break;
        }
        mbr = memory[address];
    }

    public static void pcZero(Instructions instructions) { //content=0, gpr==0, pc = EA, else pc = pc+1
        int address;
        address = FindEA(instructions);
        if (gpr[instructions.gpr_de] == 0) {
            pc = address;
        } else {
            pc++;
        }
    }

    public static void pcNotZero(Instructions instructions) {  //content!=0, gpr==0, pc = EA, else pc = pc+1
        int address;
        address = FindEA(instructions);
        if (gpr[instructions.gpr_de] != 0) {
            pc = address;
        } else {
            pc++;
        }
    }
}

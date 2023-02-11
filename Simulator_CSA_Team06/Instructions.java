package simulator;

public class Instructions {

    int op_de;
    int gpr_de;
    int ixr_de;
    int i_de;
    int addr_de;

    public Instructions(int instruction) {
        String format = String.format("%16s", Integer.toBinaryString(0xFFFF & instruction));
        String op = format.replaceAll(" ", "0").substring(0, 6);
        op_de = Integer.parseInt(op, 2);

        String gpr = format.replaceAll(" ", "0").substring(6, 8);
        gpr_de = Integer.parseInt(gpr, 2);

        String ixr = format.replaceAll(" ", "0").substring(8, 10);
        ixr_de = Integer.parseInt(ixr, 2);

        String i = format.replaceAll(" ", "0").substring(10, 11);
        i_de = Integer.parseInt(i, 2);

        String addr = format.replaceAll(" ", "0").substring(11);
        addr_de = Integer.parseInt(addr, 2);

    }
}

package simulator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;


public class FXMLController implements Initializable {

    public static int num = 0;
    public static String ins;
    public static String Op_code;
    public static String GPR_code;
    public static String IXR_code;
    public static String I_code;
    public static String Address_code;

    String[] process = new String[2048];
    String result = "";


    @FXML
    private Button Op_1, Op_2, Op_3, Op_4, Op_5, Op_6,
            GPR_1, GPR_2,
            IXR_1, IXR_2,
            I,
            Adr_1, Adr_2, Adr_3, Adr_4, Adr_5;

    @FXML
    private TextField GPR0_Text, GPR1_Text, GPR2_Text, GPR3_Text,
            IXR1_Text, IXR2_Text, IXR3_Text, PC_Text,
            MAR_Text, MBR_Text, MFR_Text, IR_Text;

    @FXML
    private TextArea res_Text;


    void resetButton(Button btn) {
        btn.setText("0");
    }

    void initializePanel() {
        resetButton(Op_1);
        resetButton(Op_2);
        resetButton(Op_3);
        resetButton(Op_4);
        resetButton(Op_5);
        resetButton(Op_6);
        resetButton(GPR_1);
        resetButton(GPR_2);
        resetButton(IXR_1);
        resetButton(IXR_2);
        resetButton(I);
        resetButton(Adr_1);
        resetButton(Adr_2);
        resetButton(Adr_3);
        resetButton(Adr_4);
        resetButton(Adr_5);
    }

    @FXML
    void SS() { //single step btn
        Instructions ins = SimFunc.singleStep();
        res_Text.setText("executed " + ins.op_de);
    }

    @FXML
    void change_ins(ActionEvent event) { //manual input buttons (in binary format)
        Object source = event.getSource();
        if (num == 0) {
            ((Button) source).setText("1");
            num = 1;
        } else {
            ((Button) source).setText("0");
            num = 0;
        }
    }

    @FXML
    void load_code() { //input code send to Instruction for breakdown
        int input_code = convert216(getIns());
        Instructions code = new Instructions(input_code);
        LDBtnFunc(code);
    }

    @FXML
    void store_code() { //input code send to Instruction for breakdown
        int input_code = convert216(getIns());
        Instructions code = new Instructions(input_code);
        STRBtnFunc(code);
    }

    @FXML
    void readFile() { //IPL btn function
        initializePanel();
        SimFunc.IPL();
        show();
        res_Text.setText("file loaded...");
        /*
          memory[0]:79
          memory[10]:9
          memory[32]:10
         */
    }

    String getIns() { //get instruction codes which you manually input
        Op_code = Op_1.getText()
                + Op_2.getText() + Op_3.getText() + Op_4.getText()
                + Op_5.getText() + Op_6.getText();
        GPR_code = GPR_1.getText() + GPR_2.getText();
        IXR_code = IXR_1.getText() + IXR_2.getText();
        I_code = I.getText();
        Address_code = Adr_1.getText() + Adr_2.getText()
                + Adr_3.getText() + Adr_4.getText() + Adr_5.getText();
        ins = Op_code + GPR_code + IXR_code + I_code + Address_code;
        return ins;
    }


    @FXML
    void Run() { //Run button function
        SimFunc.run();
        Res(SimFunc.mar);
        show();
    }


    void Res(int index) { //show results one by one
        process[SimFunc.counter] = "memory[" + index + "]:" + SimFunc.memory[index];
        result += process[SimFunc.counter] + "\n";
        res_Text.setText(result);
        res_Text.setScrollTop(Double.MAX_VALUE);
        SimFunc.counter++;

    }

    void LDBtnFunc(Instructions instructions) { //Load button function
        SimFunc.LDA(instructions);
        compareType(instructions);
    }

    void STRBtnFunc(Instructions instructions) { //Store button function
        SimFunc.STR(instructions);
        res_Text.setText("Stored...");
    }

    public int convert216(String ins) { //convert binary to hexadecimal
        return Integer.parseInt(ins, 2);
    }

    public void compareType(Instructions ins) { //show GPR[i]s status in their own column, also shows their content in decimals
        switch (ins.gpr_de) {
            case 0:
                GPR0_Text.setText(getIns());
                res_Text.setText("GPR[0]:" + SimFunc.gpr[0]);
                break;
            case 1:
                GPR1_Text.setText(getIns());
                res_Text.setText("GPR[1]:" + SimFunc.gpr[1]);
                break;
            case 2:
                GPR2_Text.setText(getIns());
                res_Text.setText("GPR[2]:" + SimFunc.gpr[2]);
                break;
            case 3:
                GPR3_Text.setText(getIns());
                res_Text.setText("GPR[3]:" + SimFunc.gpr[3]);
                break;
        }
    }

    void show() { //data button function, show every data in every column in their respective formats

        GPR0_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.gpr[0])).replaceAll(" ", "0"));
        GPR1_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.gpr[1])).replaceAll(" ", "0"));
        GPR2_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.gpr[2])).replaceAll(" ", "0"));
        GPR3_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.gpr[3])).replaceAll(" ", "0"));

        IXR1_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.indexRegister1)).replaceAll(" ", "0"));
        IXR2_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.indexRegister2)).replaceAll(" ", "0"));
        IXR3_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.indexRegister3)).replaceAll(" ", "0"));

        PC_Text.setText(String.format("%12s", Integer.toBinaryString(0xFFFF & SimFunc.pc)).replaceAll(" ", "0"));
        MAR_Text.setText(String.format("%12s", Integer.toBinaryString(0xFFFF & SimFunc.mar)).replaceAll(" ", "0"));
        MBR_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.mbr)).replaceAll(" ", "0"));
        IR_Text.setText(String.format("%16s", Integer.toBinaryString(0xFFFF & SimFunc.ir)).replaceAll(" ", "0"));
        MFR_Text.setText(String.format("%4s", Integer.toBinaryString(0xFFFF & SimFunc.ir)).replaceAll(" ", "0"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}



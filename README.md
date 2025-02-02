🖥️ CISC Simulator

📌 Overview

The CISC Simulator is a project designed to simulate a Complex Instruction Set Computing (CISC) processor. It allows users to execute instructions, interact with registers, and monitor memory operations while closely replicating a CISC-based computing environment.

The simulator supports Single Step Mode and IPL Mode, enabling users to either manually control execution or rely on automatic memory loading. The project consists of a memory class, register class, instruction decoder, backend class for execution, and an ISA class for effective address calculation.


🚀 Features

• Instruction Execution: Supports STR, LDA, LDX, and STX operations.

• Single Step Execution: Manually execute each instruction step by step.

• Preloaded Memory (IPL Mode): Automatically loads instructions from ROM.

• Register Management: Handles X1 to X3 and R0 to R3 registers.

• Memory Operations: Load and store values in memory and registers.

• Fetch & Execute Simulation: Implements MAR, MBR, IR, and PC for instruction execution.

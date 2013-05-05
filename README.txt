
liweg, a light weight graphical machine

  Far smaller than many virtual machines, liweg is designed for
  application programming on a very small scale.  

  The objective for applications of liweg is to rapidly and securely
  replace an application programming layer of software by the end user
  of an embedded system.

  The virtual machine is extended to the application hardware and
  software requirements for end user programming.  The virtual machine
  is adapted to a graphical display with a synchronous or asynchronous
  framebuffer model for liweg programming.

  The virtual machine or its boot loader is an embedded firmware.  The
  embedded firmware update process is responsible for the integrity of
  the virtual machine.  The virtual machine is responsible for running
  the programming provided by the application communications
  architecture.

  Liweg includes features for structured programming and simple raster
  graphics.  

  The liweg machine has no goto instruction.  It interprets structured
  conditional blocks from the assembler layer.  

  Liweg machine code maintains the end user's conditional, loop, and
  function structures.  This design choice shifts a burden from the
  implementation of compiler and assembler code generation to virtual
  machine design and implementation, and permits the disassembly of
  liweg machine code to reproduce the original code structure.

  The instruction stream has variable length operator code assemblies.
  Liweg operators have an eight bit instruction code, and express a
  fixed number of arguments.  Instruction codes are refined
  (specified) with kind and type qualifiers for each argument.
  Instruction parameter data has a byte aligned binary value.

  The mechanical model includes registers and functions.  The
  registers available to a function include arguments and variables.

  A register block is a call stack frame that provides specific scopes
  of execution.  As the function can only access registers provided by
  reference or value by the assembly process, the assembler "sees" or
  "knows" the access scope of each function.  This structure in
  assembly and runtime virtual machine constrains the runtime
  evaluation of the function for the benefit of tooling (human
  computer interface).

  A liweg virtual machine may define registers available to its
  programs for virtual devices.  A liweg assembler needs to know these
  registers in location index and data type.  

  The assembler may have agreement with the virtual machine for the
  maximum number of registers and the corresponding maximum bit length
  of their locations.  The assembler is free to pack indeces and other
  integer values into the smallest number of bytes that will fit their
  value, because the instruction data type specifies instruction data
  size and the virtual machine is required to widen and truncate types
  as necessary to their use.
  

Directory structure

  ./tools/

    The assembler and disassembler are in java

  ./vm/

    The virtual machine is in C


Author

  John Pritchard
  Syntelos
  28 Florence Avenue
  Leonardo, NJ
  07737


Licensing

  Planned Open Source




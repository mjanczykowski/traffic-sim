# Traffic simulation for HPC

# Running

1. Run SupervisorApp
2. Run as many instances of WorkerApp as specified in worker.nodes property inside supervisor.conf file (default: 2). For each WorkerApp, specify its akka.remote.netty.tcp.port e.g.
- WorkerApp 1: -Dakka.remote.netty.tcp.port=6671
- WorkerApp 2: -Dakka.remote.netty.tcp.port=6672
3. Simulation will start automatically when all workers are connected to the SupervisorApp.
4. Optionally run CityVisualizerMain to observe graphical state of selected node (NOTE: frame counters are not updated by default not to generate additional unneccessary network traffic)

# Configuration

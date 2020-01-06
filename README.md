# Traffic simulation for HPC

## Running

1. Run SupervisorApp
2. Run as many instances of WorkerApp as specified in `worker.nodes` property inside **supervisor.conf** file (default: 2). For each WorkerApp, specify its port e.g.
   - WorkerApp 1: -Dakka.remote.netty.tcp.port=6671
   - WorkerApp 2: -Dakka.remote.netty.tcp.port=6672
3. The simulation will start automatically when all workers are connected to the SupervisorApp.
4. Optionally run CityVisualizerMain to observe the graphical state of the selected area (NOTE: frame counters are not updated by default not to generate additional unnecessary network traffic). During visualization, the simulation on the displayed area is slowed down to 1000ms per frame.

## Configuration

1. Configure simulation settings inside **trafficsimulation.conf** file.
   - `trafficsimulation.area.*` - single city area parameters
   - `trafficsimulation.vehicle.*` - acceleration and max velocity of each car
   - `trafficsimulation.visualization.*` - single road cell size and single frame duration
   - `trafficsimulation.city.*` - controls dimenstions of the city. The total number of areas is cols\*rows.
   - `trafficsimulation.time.seconds` - duration of the whole simulation
   - `trafficsimulation.desynchronization.limit` - allows to set max time difference between neighouring areas
2. Configure supervisor settins in **supervisor.conf** file.
   - `worker.nodes` - number of running worker instances
   - `worker.areas_per_node` - number of city areas to be simulated on a single node. **Please make sure that worker.nodes\*worker.areas_per_node is equal to trafficsimulation.city.cols\*trafficsimulation.city.rows**

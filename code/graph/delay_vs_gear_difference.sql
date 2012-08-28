SELECT
  schedules.name,
  simulation_runs.control_policy,
  boats_launched,
  delay as xcol,
  AVG(avg_landed_gear_difference_in_run) as ycol
FROM simulation_runs
JOIN (SELECT 
  simulation_run_id,
  COUNT(boat_records.id) as boats_launched,
  SUM(IF(boat_records.land_tick IS NOT NULL, 1, 0)) as boats_landed,
  MAX(boat_records.launch_tick)/(COUNT(boat_records.id)-1) as delay,
  SUM(IF(boat_records.land_tick IS NOT NULL, boat_records.aggregate_gear_difference,0))/SUM(IF(boat_records.land_tick IS NOT NULL, 1, 0)) as avg_landed_gear_difference_in_run
  FROM boat_records
  GROUP BY simulation_run_id
) AS aggregate_boat_records ON aggregate_boat_records.simulation_run_id = simulation_runs.id
JOIN simulation_parameters ON simulation_parameters.id = simulation_runs.simulation_parameters_id
JOIN schedules ON schedules.id = simulation_parameters.schedule_id
WHERE simulation_runs.flushed = 1
AND simulation_runs.control_policy = '%s'
AND boats_launched = %d
GROUP BY schedules.name
ORDER BY xcol
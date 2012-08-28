SELECT
  simulation_runs.control_policy,
  AVG(aggregate_crash_records.crash_count) as ycol,
  delay as xcol
FROM simulation_runs
JOIN (SELECT 
  simulation_run_id,
  COUNT(boat_records.id) as boats_launched,
  SUM(IF(boat_records.land_tick IS NOT NULL, 1, 0)) as boats_landed,
  MAX(boat_records.launch_tick)/(COUNT(boat_records.id)-1) as delay
  FROM boat_records
  GROUP BY simulation_run_id
) AS aggregate_boat_records ON aggregate_boat_records.simulation_run_id = simulation_runs.id
LEFT JOIN (SELECT
  simulation_run_id,
  COUNT(crash_records.id) as crash_count
  FROM crash_records
  GROUP BY simulation_run_id
) AS aggregate_crash_records ON aggregate_crash_records.simulation_run_id = simulation_runs.id
JOIN simulation_parameters ON simulation_parameters.id = simulation_runs.simulation_parameters_id
JOIN schedules ON schedules.id = simulation_parameters.schedule_id
WHERE simulation_runs.flushed = 1
AND simulation_runs.control_policy = '%s'
AND boats_launched = %d
GROUP BY boats_launched, delay, simulation_runs.control_policy
ORDER BY xcol
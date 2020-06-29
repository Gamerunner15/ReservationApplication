
--ORIGINAL QUERY FOR AVAILABLE SPACES
SELECT space.id, space.name, venue.name, CAST(space.daily_rate AS decimal), space.open_from, space.open_to, space.max_occupancy, space.is_accessible FROM space 
JOIN venue ON venue.id = space.venue_id 
WHERE venue_id = 1  AND max_occupancy >= 50 
AND NOT EXISTS (SELECT * FROM reservation 
WHERE (CAST('2020-08-19' AS DATE) BETWEEN reservation.start_date AND reservation.end_date 
OR CAST('2020-08-21' AS DATE) BETWEEN reservation.start_date AND reservation.end_date) 
AND reservation.space_id = space.id) 
AND ((EXTRACT(MONTH from DATE '2020-08-19') BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) 
AND ((EXTRACT(MONTH from DATE '2020-08-21') BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) 
GROUP BY space.id, venue.name ORDER BY CAST(space.daily_rate AS decimal) ASC LIMIT 5;

--BONUS QUERY
SELECT  venue.name AS venue_name, space.id, space.name, CAST(space.daily_rate AS decimal), space.open_from, space.open_to, space.max_occupancy, space.is_accessible, category.name FROM space 
JOIN venue ON venue.id = space.venue_id 
JOIN category_venue ON category_venue.venue_id = venue.id
JOIN category ON category.id = category_venue.category_id
WHERE max_occupancy >= 10
AND category.id = 2
AND (space.is_accessible = true OR space.is_accessible = false)
AND CAST(space.daily_rate AS decimal) <= CAST(3800 AS decimal)

AND NOT EXISTS (SELECT reservation_id FROM reservation 
WHERE (CAST('2020-09-19' AS DATE) BETWEEN reservation.start_date AND reservation.end_date 
OR CAST('2020-09-21' AS DATE) BETWEEN reservation.start_date AND reservation.end_date) 
AND reservation.space_id = space.id) 
AND ((EXTRACT(MONTH from DATE '2020-09-19') BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) 
AND ((EXTRACT(MONTH from DATE '2020-09-21') BETWEEN space.open_from AND space.open_to) OR space.open_from IS NULL AND space.open_to IS NULL) 
GROUP BY space.id, venue.name, venue.id, category.name ORDER BY venue.id, CAST(space.daily_rate AS decimal);

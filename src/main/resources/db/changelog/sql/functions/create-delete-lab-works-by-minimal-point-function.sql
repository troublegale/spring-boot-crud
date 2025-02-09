CREATE OR REPLACE FUNCTION delete_lab_works_by_minimal_point(p INTEGER, u_id BIGINT)
    RETURNS BOOLEAN AS
'
DECLARE
    deleted BOOLEAN;
BEGIN
    DELETE
    FROM lab_works
    WHERE id IN
          (SELECT id
           FROM lab_works
           WHERE minimal_point = p AND user_id = u_id
           LIMIT 1)
    RETURNING TRUE INTO deleted;
    IF deleted IS NULL THEN
        deleted := FALSE;
    END IF;
    RETURN deleted;
END;
' LANGUAGE plpgsql;
CREATE OR REPLACE FUNCTION delete_lab_work_by_minimal_point(p INTEGER, u_id BIGINT)
    RETURNS BIGINT AS
'
DECLARE
    deleted_id BIGINT;
BEGIN
    DELETE
    FROM lab_works
    WHERE id IN
          (SELECT id
           FROM lab_works
           WHERE minimal_point = p AND user_id = u_id
           LIMIT 1)
    RETURNING id INTO deleted_id;
    IF deleted_id IS NULL THEN
        deleted_id := -1;
    END IF;
    RETURN deleted_id;
END;
' LANGUAGE plpgsql;
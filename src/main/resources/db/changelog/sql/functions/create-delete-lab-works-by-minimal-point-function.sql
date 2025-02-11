CREATE OR REPLACE FUNCTION delete_lab_work_by_minimal_point(p INTEGER, u_id BIGINT)
    RETURNS BIGINT AS
'
    DECLARE
        deleted_id BIGINT;
        u_role     varchar;
    BEGIN
        SELECT role
        FROM users
        WHERE id = u_id
        INTO u_role;
        IF u_role = ''ROLE_ADMIN'' THEN
            DELETE
            FROM lab_works
            WHERE id IN (SELECT id
                         FROM lab_works
                         WHERE minimal_point = p
                         LIMIT 1)
            RETURNING id INTO deleted_id;
        ELSE
            DELETE
            FROM lab_works
            WHERE id IN
                  (SELECT id
                   FROM lab_works
                   WHERE minimal_point = p
                     AND user_id = u_id
                   LIMIT 1)
            RETURNING id INTO deleted_id;
        END IF;
        IF deleted_id IS NULL THEN
            deleted_id := -1;
        END IF;
        RETURN deleted_id;
    END;
' LANGUAGE plpgsql;
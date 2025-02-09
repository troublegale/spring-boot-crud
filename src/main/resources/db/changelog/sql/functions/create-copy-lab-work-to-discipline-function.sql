CREATE OR REPLACE FUNCTION copy_lab_work_to_discipline(lab_id BIGINT, dis_id BIGINT, u_id BIGINT)
    RETURNS SETOF lab_works AS
'
DECLARE
    new_id BIGINT;
BEGIN
    INSERT INTO lab_works(average_point, creation_date, description, difficulty, minimal_point, name, author_id,
                          coordinates_id, discipline_id, user_id)
    SELECT average_point,
           CURRENT_TIMESTAMP(6),
           description,
           difficulty,
           minimal_point,
           name,
           author_id,
           coordinates_id,
           dis_id,
           u_id
    FROM lab_works
    WHERE id = lab_id
    RETURNING id INTO new_id;

    RETURN QUERY SELECT *
                 FROM lab_works
                 WHERE id = new_id;
END;
' LANGUAGE plpgsql;

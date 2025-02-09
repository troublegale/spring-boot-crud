CREATE OR REPLACE FUNCTION get_count_by_author_id(a_id BIGINT)
    RETURNS INTEGER AS
'
BEGIN
    RETURN (SELECT COUNT(*)
            FROM lab_works
            WHERE author_id = a_id);
END;
' LANGUAGE plpgsql;

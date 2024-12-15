create or replace function delete_lab_work_by_minimal_point(p integer, u_id bigint)
    returns boolean as
'
    declare
        deleted boolean;
    begin
        delete
        from lab_works
        where id in
              (select id
               from lab_works
               where minimal_point = p and user_id = u_id
               limit 1)
        returning true into deleted;
        IF deleted IS NULL THEN
            deleted := FALSE;
        END IF;
        return deleted;
    end;
' language plpgsql;

create or replace function get_count_by_author_id(a_id bigint)
    returns integer as
'
    begin
        return (select count(*)
                from lab_works
                where author_id = a_id);
    end;
' language plpgsql;

create or replace function get_lab_works_with_description_containing(d varchar, page integer, page_size integer)
    returns setof lab_works as
'
    begin
        return query
            select *
            from lab_works
            where description like ''%'' || d || ''%''
            order by id
            offset page * page_size limit page_size;
    end;
' language plpgsql;

create or replace function adjust_lab_work_difficulty_by(lab_id bigint, steps integer, diff_vals varchar[])
    returns varchar as
'
    declare
        len   integer := cardinality(diff_vals);
        diff  varchar;
        index integer;
    begin
        select difficulty
        into diff
        from lab_works
        where id = lab_id;

        for i in 1..len
            loop
                index = i - 1;
                exit when diff = diff_vals[i];
            end loop;

        index = abs(mod(index + steps + len, len));
        diff = diff_vals[index + 1];

        if diff is null then
            return index;
        end if;

        update lab_works
        set difficulty = diff
        where id = lab_id;

        return diff;

    end;
' language plpgsql;

create or replace function copy_lab_to_discipline(lab_id bigint, dis_id bigint, u_id bigint)
    returns setof lab_works as
'
    declare
        new_id bigint;
    begin
        insert into lab_works(average_point, creation_date, description, difficulty, minimal_point, name, author_id,
                              coordinates_id, discipline_id, user_id)
        select average_point,
               CURRENT_TIMESTAMP(6),
               description,
               difficulty,
               minimal_point,
               name,
               author_id,
               coordinates_id,
               dis_id,
               u_id
        from lab_works
        where id = lab_id
        returning id into new_id;
        return query select *
                     from lab_works
                     where id = new_id;
    end;
' language plpgsql;

-- Eliminar la restricción UNIQUE sobre el campo user_id en la tabla tokens
ALTER TABLE tokens DROP CONSTRAINT IF EXISTS tokens_user_id_key;

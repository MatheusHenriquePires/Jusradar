UPDATE usuarios SET role = 'ADVOGADO' WHERE role IS NULL OR role = 'USER';

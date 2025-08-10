SELECT setval(
          pg_get_serial_sequence('order', 'id'),
          (SELECT COALESCE(MAX(id), 0) FROM "order")
        );
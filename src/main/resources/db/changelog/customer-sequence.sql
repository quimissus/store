SELECT setval(
          pg_get_serial_sequence('customer', 'id'),
          (SELECT COALESCE(MAX(id), 0) FROM customer)
        );
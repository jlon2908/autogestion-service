-- Habilita la extensión uuid-ossp (haz esto solo una vez por base de datos)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp" WITH SCHEMA public;

-- (El comentario es opcional)
COMMENT ON EXTENSION "uuid-ossp" IS 'generate universally unique identifiers (UUIDs)';

-- Crea la tabla cart
CREATE TABLE public.cart (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    user_id uuid NOT NULL,
    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    updated_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

-- Crea la tabla cart_item
CREATE TABLE public.cart_item (
    id uuid DEFAULT public.uuid_generate_v4() NOT NULL,
    cart_id uuid NOT NULL,
    sku text NOT NULL,
    quantity integer NOT NULL,
    added_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT cart_item_quantity_check CHECK ((quantity > 0))
);

-- Llave primaria para cart
ALTER TABLE ONLY public.cart
    ADD CONSTRAINT cart_pkey PRIMARY KEY (id);

-- Llave primaria para cart_item
ALTER TABLE ONLY public.cart_item
    ADD CONSTRAINT cart_item_pkey PRIMARY KEY (id);

-- Llave foránea cart_item -> cart
ALTER TABLE ONLY public.cart_item
    ADD CONSTRAINT cart_item_cart_id_fkey FOREIGN KEY (cart_id) REFERENCES public.cart(id) ON DELETE CASCADE;
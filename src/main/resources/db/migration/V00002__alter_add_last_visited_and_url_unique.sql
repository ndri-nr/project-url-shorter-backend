ALTER TABLE public.mst_url ADD last_visited timestamp NULL;
ALTER TABLE public.mst_url ADD CONSTRAINT url_un UNIQUE (url_id);

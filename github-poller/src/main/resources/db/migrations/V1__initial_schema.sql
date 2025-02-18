CREATE TABLE deployments (
                             id SERIAL PRIMARY KEY,
                             resource_id TEXT NOT NULL,
                             repo TEXT NOT NULL,
                             path TEXT NOT NULL,
                             last_hash TEXT NOT NULL,
                             last_check TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
                             last_deployment TIMESTAMP WITH TIME ZONE,
                             created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_deployments_resource ON deployments(resource_id);
CREATE INDEX idx_deployments_repo_path ON deployments(repo, path);
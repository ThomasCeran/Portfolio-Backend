-- Extend contact_message with optional phone
ALTER TABLE contact_message ADD COLUMN IF NOT EXISTS phone VARCHAR(50);

-- Extend project with new descriptive fields
ALTER TABLE project
    ADD COLUMN IF NOT EXISTS content TEXT,
    ADD COLUMN IF NOT EXISTS client VARCHAR(255),
    ADD COLUMN IF NOT EXISTS testimonial TEXT,
    ADD COLUMN IF NOT EXISTS testimonial_author VARCHAR(255),
    ADD COLUMN IF NOT EXISTS mood VARCHAR(255),
    ADD COLUMN IF NOT EXISTS personal_note TEXT,
    ADD COLUMN IF NOT EXISTS duration VARCHAR(255),
    ADD COLUMN IF NOT EXISTS role VARCHAR(255);

-- New collections
CREATE TABLE IF NOT EXISTS project_stack (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    stack_item TEXT NOT NULL,
    PRIMARY KEY (project_id, stack_item)
);

CREATE TABLE IF NOT EXISTS project_features (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    feature TEXT NOT NULL,
    PRIMARY KEY (project_id, feature)
);

CREATE TABLE IF NOT EXISTS project_contributions (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    contribution TEXT NOT NULL,
    PRIMARY KEY (project_id, contribution)
);

CREATE TABLE IF NOT EXISTS project_outcomes (
    project_id UUID NOT NULL REFERENCES project (id) ON DELETE CASCADE,
    outcome TEXT NOT NULL,
    PRIMARY KEY (project_id, outcome)
);

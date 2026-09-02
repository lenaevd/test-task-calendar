CREATE TABLE IF NOT EXISTS calendar_templates
(
    id SERIAL PRIMARY KEY,
    first_day_of_week VARCHAR(9) NOT NULL,
    is_leap BOOLEAN NOT NULL,
    CONSTRAINT uq_calendar UNIQUE(first_day_of_week, is_leap)
    );

CREATE TABLE IF NOT EXISTS months
(
    id SERIAL PRIMARY KEY,
    calendar_template_id INT NOT NULL REFERENCES calendar_templates(id) ON DELETE CASCADE,
    month_number INT NOT NULL CHECK (month_number BETWEEN 1 AND 12),
    days_count INT NOT NULL,
    first_day_of_week VARCHAR(9) NOT NULL,
    CONSTRAINT uq_month UNIQUE(calendar_template_id, month_number)
);


# 0001. Starting from modular monolith
Date: 2026-09-07

## Context
System sells tickets for events. The main complexity is the competition for a limited resource: thousands of simultaneous tries to take the last places. In the beginning there is no information about load profile, no established boundaries og the subject area.

## Solution
One app, one process, one database. There are modules: catalog, inventory, orders with the strict boundaries: public api and closed internal. or each module there is separate schema of database, there is no external keys between schemas.

## Reasoning
The boundaries of the domain at the start are the hypothesis. An error in the boundaries within the monolith is corrected by moving classes between packages. 
The same error after splitting into services means changing contracts, migrating data and consistent deployment.

The api/internal discipline and separate schemas make subsequent splitting cheap: moving a module means replacing a method call with network call and moving schema, rather than rewriting it.

## Rejected
- Three services from the beginning. It makes distributed monolith: network delays and distributed transactions appear immediately, but deployment and scaling independence do not because the services remain data-bound.

- A monolith without internal boundaries. It is faster to write, but after a month the modules become intertwined through shared entities, and separation becomes impossible without rewriting.

## Effects
There is no independent scaling of individual parts. Boundaries are maintained by developer discipline, not by compiler - until the automatic rule check is enabled

## Review when
The module will have a load proile different from the others, or there will be a need for a separate deployment cycle. The expected first candidate is inventory.
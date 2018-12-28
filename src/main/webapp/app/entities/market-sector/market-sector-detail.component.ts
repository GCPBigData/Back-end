import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMarketSector } from 'app/shared/model/market-sector.model';

@Component({
    selector: 'jhi-market-sector-detail',
    templateUrl: './market-sector-detail.component.html'
})
export class MarketSectorDetailComponent implements OnInit {
    marketSector: IMarketSector;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ marketSector }) => {
            this.marketSector = marketSector;
        });
    }

    previousState() {
        window.history.back();
    }
}

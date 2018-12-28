import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStockWatch } from 'app/shared/model/stock-watch.model';
import { StockWatchService } from './stock-watch.service';

@Component({
    selector: 'jhi-stock-watch-delete-dialog',
    templateUrl: './stock-watch-delete-dialog.component.html'
})
export class StockWatchDeleteDialogComponent {
    stockWatch: IStockWatch;

    constructor(private stockWatchService: StockWatchService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.stockWatchService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'stockWatchListModification',
                content: 'Deleted an stockWatch'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-stock-watch-delete-popup',
    template: ''
})
export class StockWatchDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ stockWatch }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StockWatchDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.stockWatch = stockWatch;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
